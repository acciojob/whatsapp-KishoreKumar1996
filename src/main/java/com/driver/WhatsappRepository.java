package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private Map<String,User> userMobile;
    private int customGroupCount;
    private int messageId;

    private HashMap<String,String> user ;
    private HashMap<Integer,String> message;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashMap<>();
        this.customGroupCount = 0;
        this.messageId = 0;
//        this.user=new HashMap<String,String>();
//        this.message=new HashMap<>();
    }

    public boolean newUser(String mobile) {
        if(userMobile.containsKey(mobile)) return false;
        return true;
    }

    public void createUser(String name, String mobile) {
        userMobile.put(mobile, new User(name, mobile));
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if(!adminMap.get(group).equals(approver)) throw new Exception("Approver does not have rights");
        if(!this.user(group, user)) throw  new Exception("User is not a participant");

        adminMap.put(group, user);
        return "SUCCESS";
    }
    public boolean user(Group group, User sender) {
        List<User> users = groupUserMap.get(group);
        for(User user: users) {
            if(user.equals(sender)) return true;
        }
        return false;
    }

    public Group createGroup(List<User> users) {
        if(users.size() == 2) return this.createPersonalChat(users);

        this.customGroupCount++;
        String groupName = "Group " + this.customGroupCount;
        Group group = new Group(groupName, users.size());
        groupUserMap.put(group, users);
        adminMap.put(group, users.get(0));
        return group;
    }

    public Group createPersonalChat(List<User> users) {
        String groupName = users.get(1).getName();
        Group personalGroup = new Group(groupName, 2);
        groupUserMap.put(personalGroup, users);
        return personalGroup;
    }

    public int createMessage(String content){
        this.messageId++;
        Message message = new Message(messageId, content, new Date());
        return this.messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if(!this.user(group, sender)) throw  new Exception("You are not allowed to send message");

        List<Message> messages = new ArrayList<>();
        if(groupMessageMap.containsKey(group)) messages = groupMessageMap.get(group);

        messages.add(message);
        groupMessageMap.put(group, messages);
        return messages.size();
    }


}
