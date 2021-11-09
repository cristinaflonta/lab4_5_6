package ro.ubb.exam.Domain.Validators;

import ro.ubb.exam.Domain.Entity;

public abstract class Validator<T extends Entity>  {

    public abstract void validate(T entity);

}
