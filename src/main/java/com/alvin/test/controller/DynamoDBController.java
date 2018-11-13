package com.alvin.test.controller;

import com.alvin.test.dynamodb.DynamoDBUtil;
import com.alvin.test.entity.Person;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DynamoDBController {
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
        return "";
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
}
