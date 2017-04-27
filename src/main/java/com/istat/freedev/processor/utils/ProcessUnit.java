package com.istat.freedev.processor.utils;

import com.istat.freedev.processor.ProcessManager;
import com.istat.freedev.processor.Processor;

/**
 * Created by istat on 07/02/17.
 */

public class ProcessUnit {
    private String nameSpace;
    Processor processor;

    protected ProcessUnit() {
        this.nameSpace = this.getClass().getCanonicalName();
        processor = Processor.from(this.nameSpace);
    }

    protected ProcessUnit(String nameSpace) {
        this.nameSpace = nameSpace;
        processor = Processor.from(this.nameSpace);
    }

    public Processor getProcessor() {
        return processor;
    }

    public ProcessManager getProcessManager() {
        return processor.getProcessManager();
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public boolean cancel() {
        return getProcessManager().cancelAll() > 0;
    }
}
