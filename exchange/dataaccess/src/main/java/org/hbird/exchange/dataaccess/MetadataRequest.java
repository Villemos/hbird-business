package org.hbird.exchange.dataaccess;

import static org.hbird.exchange.dataaccess.Arguments.APPLICABLE_TO;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.EntityInstance;

public class MetadataRequest extends DataRequest {

    private static final long serialVersionUID = -7231902449829794969L;

    public static final String DESCRIPTION = "A request to retrieve metadata related to a Named object.";
    
    public MetadataRequest(String issuedBy, EntityInstance applicableTo) {
        super(issuedBy);

        this.setArgumentValue(StandardArguments.APPLICABLE_TO, applicableTo.getID());
    }

    /**
     * @see org.hbird.exchange.dataaccess.DataRequest#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args = super.getArgumentDefinitions(args);
        args.add(create(APPLICABLE_TO));
        return args;
    }
}
