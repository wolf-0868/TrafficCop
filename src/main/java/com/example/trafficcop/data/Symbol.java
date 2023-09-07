package com.example.trafficcop.data;

import lombok.Getter;

public enum Symbol {

    A('А'),
    B('В'),
    E('Е'),
    K('К'),
    M('М'),
    H('Н'),
    O('О'),
    P('Р'),
    C('С'),
    T('Т'),
    Y('У'),
    X('Х');

    @Getter
    private final char desc;

    Symbol(char aDesc) {
        desc = aDesc;
    }

    public static Symbol valueOfDesc(char aCh) {
        for (Symbol value : Symbol.values()) {
            if (aCh == value.getDesc()) {
                return value;
            }
        }
        return null;
    }

}
