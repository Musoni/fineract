package org.apache.fineract.commands.provider;

import org.apache.fineract.commands.annotation.CommandType;
import org.apache.fineract.commands.handler.NewCommandSourceHandler;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.springframework.stereotype.Component;

@Component
@CommandType(entity = "HUMAN", action = "UPDATE")
public class ValidCommandHandler implements NewCommandSourceHandler {

    public ValidCommandHandler() {
        super();
    }

    @Override
    public CommandProcessingResult processCommand(JsonCommand command) {
        return CommandProcessingResult.commandOnlyResult(command.commandId());
    }
}
