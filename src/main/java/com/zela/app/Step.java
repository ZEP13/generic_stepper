package com.zela.app;

import java.util.List;

public interface Step<T> {
    List<T> execute(List<T> input);
}
