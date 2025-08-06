package org.mbc.board.exception;

public class OutOfStockException extends RuntimeException {
// 주문기능
    public OutOfStockException(String message) {
      super(message);
    }

} //class 종료
