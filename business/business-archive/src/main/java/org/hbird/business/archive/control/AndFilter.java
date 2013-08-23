package org.hbird.business.archive.control;

import java.util.List;

public class AndFilter extends CombinedFilter {

    private static final long serialVersionUID = 4350809563326462776L;

    public AndFilter(List<Filter> filters) {
        super(filters);
    }

    @Override
    public boolean passes(Object obj) {
        for (Filter filter : filters) {
            if (!filter.passes(obj)) {
                return false;
            }
        }
        return true;
    }

}
