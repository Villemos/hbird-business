package org.hbird.business.archive.control;

public class NegateFilter extends Filter {
    private static final long serialVersionUID = 263584734406767035L;

    private Filter filter;

    public NegateFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public boolean passes(Object obj) {
        return !filter.passes(obj);
    }

}
