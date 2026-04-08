package com.zentrabank.bank_api.modules.refreshtoken.servce;

import com.zentrabank.bank_api.modules.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component // Makes this class a Spring-managed bean (so Spring can run it)
@RequiredArgsConstructor // Automatically injects required dependencies (constructor injection)
public class TokenCleanupScheduler {
    // Repository used to access refresh token data in DB
    private final RefreshTokenService refreshTokenService;


    /*
     *  This method runs automatically based on the cron expression
     *
     * cron = "0 0 * * * *"
     * ┌──────── second (0)
     * │ ┌────── minute (0)
     * │ │ ┌──── hour (* = every hour)
     * │ │ │ ┌── day of month (*)
     * │ │ │ │ ┌ month (*)
     * │ │ │ │ │ ┌ day of week (*)
     * │ │ │ │ │ │
     * │ │ │ │ │ │
     * 0  0  * * * *
     *
     *  Meaning: run this method EVERY HOUR at minute 0 and second 0
     */
    @Scheduled(cron = "0 0 * * * *")
    public void cleanTokens(){
        this.refreshTokenService.deleteExpiredTokens();
    }

}