package com.upconsulting.gilesecosystem.hank.model.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.upconsulting.gilesecosystem.hank.model.IOCRModel;
import com.upconsulting.gilesecosystem.hank.model.IOCRRun;
import com.upconsulting.gilesecosystem.hank.model.IRunStep;

@Entity
public class OCRRun extends Task implements IOCRRun {

    @Id
    private String id;
    private LocalDateTime date;
    private String hocrFile;

    @ManyToOne(targetEntity=OCRModel.class)
    private IOCRModel model;
    @OneToMany(cascade = CascadeType.ALL, targetEntity=RunStep.class, fetch=FetchType.EAGER)
    private List<IRunStep> steps;
    
    private String trainingId;

    /*
     * (non-Javadoc)
     * 
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRRun#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.upconsulting.gilesecosystem.hank.model.impl.IOCRRun#setId(java.lang
     * .String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRRun#getDate()
     */
    @Override
    public LocalDateTime getDate() {
        return date;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.upconsulting.gilesecosystem.hank.model.impl.IOCRRun#setDate(java.
     * util.Date)
     */
    @Override
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.upconsulting.gilesecosystem.hank.model.impl.IOCRRun#getModel()
     */
    @Override
    public IOCRModel getModel() {
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.upconsulting.gilesecosystem.hank.model.impl.IOCRRun#setModel(com.
     * upconsulting.gilesecosystem.hank.model.impl.OCRModel)
     */
    @Override
    public void setModel(IOCRModel model) {
        this.model = model;
    }

    @Override
    public List<IRunStep> getSteps() {
        if (steps == null) {
            steps = new ArrayList<IRunStep>();
        }
        return steps;
    }

    public void setSteps(List<IRunStep> steps) {
        this.steps = steps;
    }

    @Override
    public IRunStep setStepStatus(StepType type, StepStatus status) {
        Optional<IRunStep> optional = getSteps().stream()
                .filter(step -> type.equals(step.getStepType())).findFirst();

        if (optional.isPresent()) {
            IRunStep p = optional.get();
            p.setStatus(status);
            return p;
        }
        
        return null;
    }

    @Override
    public String getHocrFile() {
        return hocrFile;
    }

    @Override
    public void setHocrFile(String hocrFile) {
        this.hocrFile = hocrFile;
    }

    @Override
    public String getTrainingId() {
        return trainingId;
    }

    @Override
    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }
}
