package demo.demo;

import java.util.List;

public class CustomerRow {
    private user account;
    private List<Order> purchaseHistory;
    private List<Game> publishedGames;
    private double totalSpent;

    public CustomerRow(user account, List<Order> purchaseHistory, List<Game> publishedGames) {
        this.account = account;
        this.purchaseHistory = purchaseHistory;
        this.publishedGames = publishedGames;
        this.totalSpent = purchaseHistory.stream()
            .mapToDouble(Order::getGamePrice)
            .sum();
    }

    public user getAccount() {
        return account;
    }

    public void setAccount(user account) {
        this.account = account;
    }

    public List<Order> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(List<Order> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public List<Game> getPublishedGames() {
        return publishedGames;
    }

    public void setPublishedGames(List<Game> publishedGames) {
        this.publishedGames = publishedGames;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
}