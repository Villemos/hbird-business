package org.hbird.business.tracking.quartz;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.core.cache.EntityCache;
import org.hbird.business.tracking.TrackingComponent;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TrackingComponentDriver extends SoftwareComponentDriver<TrackingComponent> {

    public static final String TRACK_COMMAND_INJECTOR = "seda:toPublisher";

    private final IDataAccess dao;
    private final IdBuilder idBuilder;

    @Autowired
    public TrackingComponentDriver(IPublisher publisher, IDataAccess dao, IdBuilder idBuilder) {
        super(publisher);
        this.dao = dao;
        this.idBuilder = idBuilder;
    }

    @Override
    protected void doConfigure() {

        String name = entity.getName();
        TrackingDriverConfiguration config = entity.getConfiguration();
        CamelContext context = entity.getContext();
        EntityCache<GroundStation> groundStationCache = EntityCache.forType(dao, GroundStation.class);
        EntityCache<Satellite> satelliteCache = EntityCache.forType(dao, Satellite.class);
        EntityCache<TleOrbitalParameters> tleCache = EntityCache.forType(dao, TleOrbitalParameters.class);

        ProducerTemplate producer = context.createProducerTemplate();

        Scheduler scheduler;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobFactory factory = new TrackComponentJobFactory(entity, dao, producer, TRACK_COMMAND_INJECTOR, satelliteCache, tleCache, idBuilder, config);
            scheduler.setJobFactory(factory);
            scheduler.start();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to start Quartz sceduler", e);
        }

        SchedulingSupport support = new SchedulingSupport();
        ArchivePoller archivePoller = new ArchivePoller(config, dao);
        TrackCommandScheduler trackCommandScheduler = new TrackCommandScheduler(config, scheduler, support);
        TrackCommandUnscheduler contactUnscheduler = new TrackCommandUnscheduler(scheduler, support);
        EventAnalyzer analyzer = new EventAnalyzer(scheduler, support);
        ScheduleDeltaCheck deltaCheck = new ScheduleDeltaCheck(config, support);
        NotificationEventScheduler notificationScheduler = new NotificationEventScheduler(scheduler, support, groundStationCache, satelliteCache);

        // @formatter:off

        from(TRACK_COMMAND_INJECTOR)
            .choice()
                .when(body().isInstanceOf(Track.class))
                    .bean(notificationScheduler)
                .end()
                .bean(publisher);

        from("direct:scheduleContact")
            .filter(deltaCheck)
                .bean(trackCommandScheduler)
                .to("log:scheduledContact-log?level=DEBUG");

        from("direct:rescheduleContact")
            .choice()
                .when(deltaCheck)
                    .bean(contactUnscheduler)
                    .to("log:RescheduleContact-log?level=DEBUG")
                    .to("direct:scheduleContact")
                .otherwise()
                    .to("log:ReschedulingImpossible?level=WARN")
                ;

        from("seda:eventHandler")
            .to("log:handler-log?level=DEBUG")
            .process(analyzer)
            .choice()
                .when(header(EventAnalyzer.HEADER_KEY_EVENT_TYPE).isEqualTo(EventAnalyzer.HEADER_VALUE_NEW_EVENT))
                    .to("direct:scheduleContact")
                .when(header(EventAnalyzer.HEADER_KEY_EVENT_TYPE).isEqualTo(EventAnalyzer.HEADER_VALUE_UPDATED_EVENT))
                    .to("direct:rescheduleContact")
                .otherwise()
                    .log("LocationContacEvent already scheduled")
                ;

        from(addTimer(name, config.getArchivePollInterval()))
            .bean(archivePoller)
            .split(body())
            .to("seda:eventHandler");

        // @formatter:on
    }
}
