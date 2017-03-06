package com.upconsulting.gilesecosystem.hank.model;

import java.util.List;

public interface IPage {

    public abstract int getPage();

    public abstract void setPage(int page);

    public abstract List<IPageLine> getLines();

    public abstract void setLines(List<IPageLine> lines);

    public abstract void setCorrection(ICorrection correction);

    public abstract ICorrection getCorrection();

}