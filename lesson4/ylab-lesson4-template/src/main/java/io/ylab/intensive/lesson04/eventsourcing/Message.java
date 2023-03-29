package io.ylab.intensive.lesson04.eventsourcing;

public class Message {
    private Operation operation;
    private Person person;

    public  Message(Operation operation, Person person){
        this.operation = operation;
        this.person = person;
    }

    public Operation getOperation() {
        return operation;
    }

    public Person getPerson() {
        return person;
    }
}
