package com.quintor.api.mt940;

import org.springframework.stereotype.Service;

@Service
public class Mt940Service {
    private final Mt940Repository mt940Repository;

    public Mt940Service(Mt940Repository mt940Repository) {
        this.mt940Repository = mt940Repository;
    }

    public Mt940 insertMt940(Mt940 mt940) {
        return mt940Repository.insert(mt940);
    }
}
