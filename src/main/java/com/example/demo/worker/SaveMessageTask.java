package com.example.demo.worker;

import com.example.demo.dto.chat.MessageDTO;
import com.example.demo.service.MessageService;

public class SaveMessageTask implements DbTask {

    private final MessageService messageService;
    private final MessageDTO messageDTO;

    public SaveMessageTask(MessageService messageService, MessageDTO messageDTO) {
        this.messageService = messageService;
        this.messageDTO = messageDTO;
    }

    @Override
    public void execute() {
        messageService.save(messageDTO);
    }
}
