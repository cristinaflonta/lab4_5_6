package ro.ubb.exam.Domain.Validators;

import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Holiday;

public class HolidayValidator extends Validator<Holiday> {
    @Override
    public void validate(Holiday entity) throws ValidatorException {
        String destination = entity.getDestination();
        String errors = "";
        
        if (destination.charAt(0) < 'A' || destination.charAt(0) > 'Z') {
            errors += "The destination name must start with a capital letter!\n";
        }

        if (entity.getPrice() <= 0 ) {
            throw new ValidatorException("Price must be > 0 ");
        }

        if (!errors.equals("")) {
            throw new ValidatorException(errors);
        }


    }
}