package com.alvin.test.controller;

import com.alvin.test.dynamodb.DynamoDBUtil;
import com.alvin.test.entity.Person;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DynamoDBController {
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String healthCheck() {
        return "OK";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String getPerson(@RequestParam("id") final String id) {
        Table table = DynamoDBUtil.dynamoDB.getTable(DynamoDBUtil.TABLE_NAME);
        try {
            Item item = table.getItem("Id", id, "Id, Name, Gender", null);
            return item.toJSONPretty();
        } catch (Exception e) {
            System.err.println("GetItem failed.");
            System.err.println(e.getMessage());
        }
        return "Fail to get " + id;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createPerson(final Person person) {
        Table table = DynamoDBUtil.dynamoDB.getTable(DynamoDBUtil.TABLE_NAME);
        try {
            Item item = new Item().withPrimaryKey("Id", person.getId()).withString("Name", person.getName())
                    .withString("Gender", person.getGender());
            table.putItem(item);
            return item.toJSONPretty();
        } catch (Exception e) {
            System.err.println("Create failed.");
            System.err.println(e.getMessage());
        }
        return "Fail to create";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updatePerson(final Person person) {
        final Table table = DynamoDBUtil.dynamoDB.getTable(DynamoDBUtil.TABLE_NAME);
        try {
            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                    .withPrimaryKey("Id", person.getId())
                    .withUpdateExpression("set Gender = :genderNew, Name = :nameNew")
                    .withValueMap(new ValueMap()
                            .withString(":genderNew", person.getGender())
                            .withString(":nameNew", person.getName())
                    );
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            // Check the response.
            return outcome.getItem().toJSONPretty();
        } catch (Exception e) {
            System.err.println("Error updating item");
            System.err.println(e.getMessage());
        }
        return "Fail to update";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteItem(@RequestParam("id") final String id) {
        final Table table = DynamoDBUtil.dynamoDB.getTable(DynamoDBUtil.TABLE_NAME);
        try {
            DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey("Id", id);
            DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
            // Check the response.
            System.out.println("Printing item that was deleted...");
            System.out.println(outcome.getItem().toJSONPretty());
        } catch (Exception e) {
            System.err.println("Error deleting item ");
            System.err.println(e.getMessage());
        }
    }
}
