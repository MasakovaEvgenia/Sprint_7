package entity;

public class PageInfo {
    private final int page;
    private final int total;
    private final int limit;

    public PageInfo(int page, int total, int limit) {
        this.page = page;
        this.total = total;
        this.limit = limit;
    }
}
