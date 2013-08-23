package org.hbird.business.archive.control;

import java.util.List;

public class OrFilter extends CombinedFilter {

    private static final long serialVersionUID = -5199321211892657959L;

    public OrFilter(List<Filter> filters) {
        super(filters);
    }

    @Override
    public boolean passes(Object obj) {
        for (Filter filter : filters) {
            if (filter.passes(obj)) {
                return true;
            }
        }

        return false;
    }

}
