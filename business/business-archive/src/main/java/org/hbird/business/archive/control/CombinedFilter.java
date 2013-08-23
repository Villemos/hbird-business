package org.hbird.business.archive.control;

import java.util.ArrayList;
import java.util.List;

public abstract class CombinedFilter extends Filter {

    private static final long serialVersionUID = 8230912760341476796L;

    protected List<Filter> filters;

    public CombinedFilter(List<Filter> filters) {
        this.filters = new ArrayList<Filter>(filters);
    }
}
