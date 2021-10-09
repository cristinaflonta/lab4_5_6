package ro.ubb.exam.Domain.Validators;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;

public class ClientValidator extends Validator<Client> {
    @Override
    public void validate(Client entity) throws ValidatorException {
        String name = entity.getName();
        String email = entity.getEmail();


        if (name.isEmpty()){
            throw new ValidatorException("Name is mandatory");
        }

        if (email.isEmpty()){
            throw new ValidatorException("Email is mandatory");
        }


    }
}
