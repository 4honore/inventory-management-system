package controller;

import dao.TransactionDao;
import model.Transaction;

import java.sql.Timestamp;
import java.util.List;

public class TransactionController {

    private final TransactionDao transactionDao;

    public TransactionController() {
        this.transactionDao = new TransactionDao();
    }

    // ADD Transaction
    public void addTransaction(int productId, int quantity, String type, double total, Timestamp date) {
        Transaction t = new Transaction();
        t.setProductId(productId);
        t.setQuantity(quantity);
        t.setType(type);
        t.setTotal(total);
        t.setDate(date);

        transactionDao.addTransaction(t);
    }

    // GET all Transactions
    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    // DELETE Transaction
    public void deleteTransaction(int transactionId) {
        transactionDao.deleteTransaction(transactionId);
    }

    // (Optional) UPDATE Transaction
    public void updateTransaction(int id, int productId, int qty, String type, double total, Timestamp date) {
        Transaction t = new Transaction(id, productId, qty, date, type, total);
        transactionDao.updateTransaction(t);
    }

    // (Optional) GET single Transaction
    public Transaction getTransactionById(int id) {
        return transactionDao.getTransactionById(id);
    }
}
