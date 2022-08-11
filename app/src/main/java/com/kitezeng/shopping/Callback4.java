package com.kitezeng.shopping;

import com.kitezeng.shopping.Model.Product;

public interface Callback4 {
    public abstract void success(Product product);
    public abstract void fail(Throwable e);
}
