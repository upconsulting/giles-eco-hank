package com.upconsulting.gilesecosystem.hank.model.impl;

import java.util.List;

import com.upconsulting.gilesecosystem.hank.model.ICorrection;
import com.upconsulting.gilesecosystem.hank.model.IPage;
import com.upconsulting.gilesecosystem.hank.model.IPageLine;

public class Page implements IPage {

    private int page;
    private List<IPageLine> lines;
    private ICorrection correction;
    
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IPage#getPage()
     */
    @Override
    public int getPage() {
        return page;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IPage#setPage(int)
     */
    @Override
    public void setPage(int page) {
        this.page = page;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IPage#getLines()
     */
    @Override
    public List<IPageLine> getLines() {
        return lines;
    }
    /* (non-Javadoc)
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IPage#setLines(java.util.List)
     */
    @Override
    public void setLines(List<IPageLine> lines) {
        this.lines = lines;
    }
    @Override
    public ICorrection getCorrection() {
        return correction;
    }
    @Override
    public void setCorrection(ICorrection correction) {
        this.correction = correction;
    }
    
}
