package MiniTwitter;

import java.util.ArrayList;
import java.util.List;

public class UserGroup implements TwitterComponent {
    private String id;
    private List<TwitterComponent> members;

    public UserGroup(String id) {
        this.id = id;
        this.members = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<TwitterComponent> getMembers() {
        return members;
    }

    public void addMember(TwitterComponent member) {
        members.add(member);
    }

    @Override
    public void accept(Visitor visitor) {
        for (TwitterComponent member : members) {
            member.accept(visitor);
        }
        visitor.visit(this);
    }
}


