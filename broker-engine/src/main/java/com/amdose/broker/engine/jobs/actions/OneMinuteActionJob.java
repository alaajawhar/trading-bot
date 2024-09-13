package com.amdose.broker.engine.jobs.actions;

import com.amdose.broker.engine.brokers.IBrokerService;
import com.amdose.database.entities.SignalEntity;
import com.amdose.database.enums.BotModeEnum;
import com.amdose.database.repositories.ISignalRepository;
import com.amdose.scheduler.exposed.IOneMinuteJob;
import com.amdose.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OneMinuteActionJob implements IOneMinuteJob {

    private final ISignalRepository signalRepository;
    private final IBrokerService binanceBroker;

    @Override
    public void execute() {
        try {
            TimeUnit.SECONDS.sleep(1);

            log.info("Fetching actions for [{}]...", DateUtils.roundSecondsAndMilliseconds(DateUtils.getNow()));
            List<SignalEntity> pendingSignalsWithNoActions =
                    signalRepository.findAllPendingActionsOfDate(DateUtils.roundSecondsAndMilliseconds(DateUtils.getNow()));

            log.info("Found [{}] pending actions", pendingSignalsWithNoActions.size());

            for (SignalEntity signalEntity : pendingSignalsWithNoActions) {

                if (signalEntity.getBot().getMode() != BotModeEnum.TRADE) {
                    continue;
                }

                switch (signalEntity.getAction()) {
                    case BUY -> binanceBroker.buyLimit(signalEntity);
                    case SELL -> binanceBroker.sellLimit(signalEntity);
                    default -> throw new RuntimeException("Not handled action...");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}