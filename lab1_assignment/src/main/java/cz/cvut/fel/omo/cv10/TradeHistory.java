package cz.cvut.fel.omo.cv10;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class TradeHistory {

    public List<Transaction> transactions;

    public TradeHistory(List<Transaction> transctions) {
        this.transactions = transctions;
    }

    public List<Transaction> findAllTransactionsIn2011AndSortByValueAsc(){
        List<Transaction> newList = transactions.stream()
                .filter(x->x.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        return newList;
    }

    public List<String> getUniqueCitiesSortedAsc(){
        List<String> newList = new ArrayList<String>();
        newList = this.transactions.stream().map(t -> t.getTrader().getCity())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return newList;
    }

    /*
    * String shall start with "Traders:" and use space as separator. E.g.: "Traders: Bob George"
    *
     */
    public String getSingleStringFromUniqueTradersNamesSortByNameAsc(){
        String traderStr = "Traders: ";
        traderStr += this.transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted()
                .reduce((s1,s2) -> s1 += " " + s2)
                .map(Objects::toString)
                .orElse("");
        return traderStr;
    }

    public boolean isSomeTraderFromCity(String cityName){
        boolean isSome = false;
        isSome = this.transactions.stream()
                .anyMatch(t -> Objects.equals(t.getTrader().getCity(), cityName));
        return isSome;
    }

    public Optional<Transaction> findSmallestTransactionUsingReduce(){
        Optional<Transaction> smallestTransaction = transactions.stream().min(Comparator.comparing(Transaction::getValue));
        return smallestTransaction;
    }

    public Map<String, List<Trader>> getTradersByTown(){
        Map<String, List<Trader>> tradersByTown = transactions.stream()
                .flatMap(t -> Stream.of(t.getTrader()))
                .distinct()
                .collect(Collectors.groupingBy(Trader::getCity));
        return tradersByTown;
    }

    public Map<String, Long> getTradersCountsByTown(){
        Map<String, Long> tradersByTown = transactions.stream()
                .flatMap(t -> Stream.of(t.getTrader()))
                .distinct()
                .collect(Collectors.groupingBy(Trader::getCity, counting()));
        return tradersByTown;
    }

    public Map<Boolean, List<Transaction>> partitionTransactionsByTraderIsVegetarian(){
        Map<Boolean, List<Transaction>> transactionsBy = transactions.stream()
                .collect(groupingBy(t->t.getTrader().isVegetarian()));
        return transactionsBy;
    }
}
