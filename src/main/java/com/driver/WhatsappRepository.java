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
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    private HashMap<String,String> user ;
    private HashMap<Integer,String> message;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
        this.user=new HashMap<String,String>();
        this.message=new HashMap<>();
    }

    public String createUser(String name, String number)throws Exception{
        if(userMobile.contains(number)){
            throw new Exception("User already exists");
        }
        userMobile.add(number);
        user.put(name,number);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users){
        User adminName= users.get(0);

        if(users.size()==2){
            User use=users.get(users.size()-1);
            String wt=use.getName();
            Group member=new Group(wt,users.size());
            groupUserMap.put(member,users);
            adminMap.put(member,adminName);
            return member;
        }
        customGroupCount++;
        Group member=new Group("Group "+customGroupCount,users.size());
        groupUserMap.put(member,users);
        adminMap.put(member,adminName);
        return member;
    }

    public int  createMessage(String content){
        messageId++;
        message.put(messageId,content); // putting msg id with content;
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUserMap.containsKey(group.getName())){
            throw new Exception("Group does not exist");
        }
        String personName=sender.getName();
        List groupName=groupUserMap.get(group.getName());
        if(!groupName.contains(personName)){
            throw new Exception("You are not allowed to send message");
        }
        return messageId;
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }

        List<User>changeadmin=groupUserMap.get(group);
        if(!changeadmin.contains(user)){
            throw  new Exception("User is not a participant");
        }
        String groupadmin =adminMap.get(group).getName();
        if (groupadmin != approver.getName()){
            throw new Exception("Approver does not have rights");
        }
        adminMap.put(group,user);
        return "SUCCESS";

    }
}
