package org.hbird.business.tracking.quartz;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.core.cache.EntityCache;
import org.hbird.business.core.cache.SatelliteResolver;
import org.hbird.business.core.cache.TleResolver;
import org.hbird.business.tracking.TrackingComponent;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

public class TrackingComponentDriver extends SoftwareComponentDriver {

    public static final String TRACK_COMMAND_INJECTOR = "seda:track-commands";

    @Override
    protected void doConfigure() {

        String name = entity.getName();
        TrackingDriverConfiguration config = ((TrackingComponent) entity).getConfiguration();
        CamelContext context = entity.getContext();
        IDataAccess dao = ApiFactory.getDataAccessApi(name, context);
        EntityCache<Satellite> satelliteCache = new EntityCache<Satellite>(new SatelliteResolver(dao));
        EntityCache<TleOrbitalParameters> tleCache = new EntityCache<TleOrbitalParameters>(new TleResolver(dao));

        ProducerTemplate producer = context.createProducerTemplate();

        Scheduler scheduler;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobFactory factory = new TrackCommandCreationJobFactory(entity, dao, producer, TRACK_COMMAND_INJECTOR, satelliteCache, tleCache);
            scheduler.setJobFactory(factory);
            scheduler.start();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to start Quartz sceduler", e);
        }

        ArchivePoller archivePoller = new ArchivePoller(config, dao);
        ContactScheduler contactScheduler = new ContactScheduler(config, scheduler);

        // @formatter:off
        
        addInjectionRoute(from(TRACK_COMMAND_INJECTOR));
        
        from("direct:scheduleContact")
            .bean(contactScheduler)
            .to("log:scheduledContact-log?level=DEBUG");
        
        from("direct:rescheduleContact")
            .to("log:REscheduleContact-log?level=DEBUG");
        
        from("seda:eventHandler")
            .to("log:handler-log?level=DEBUG")
            // TODO - 30.04.2013, kimmell - add filtering and choose what to do with the event
            .to("direct:scheduleContact");
        
        
        from(addTimer(name, config.getArchivePollInterval()))
            .bean(archivePoller)
            .split(body())
            .to("seda:eventHandler");
        
        // @formatter:on
    }
}
