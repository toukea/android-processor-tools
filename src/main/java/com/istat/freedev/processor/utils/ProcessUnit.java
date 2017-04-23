package com.istat.freedev.processor.utils;

import com.istat.freedev.processor.ProcessManager;
import com.istat.freedev.processor.Processor;

/**
 * Created by istat on 07/02/17.
 */

public class ProcessUnit {
    Processor processor;

    protected ProcessUnit() {
        processor = Processor.from(this.getClass().getCanonicalName());
    }

    protected ProcessUnit(String processorTag) {
        processor = Processor.from(processorTag);
    }

    public Processor getProcessor() {
        return processor;
    }

    public ProcessManager getProcessManager() {
        return processor.getProcessManager();
    }
}
