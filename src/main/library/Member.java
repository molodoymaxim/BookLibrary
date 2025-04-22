package main.library;

public class Member {
    private final String memberId;
    private final String name;

    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return String.format("Member[%s]: %s", memberId, name);
    }
}
