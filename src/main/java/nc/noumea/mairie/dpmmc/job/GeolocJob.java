package nc.noumea.mairie.dpmmc.job;

import nc.noumea.mairie.dpmmc.services.interfaces.IGeolocService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DisallowConcurrentExecution
/**
 * GeolocJob is a quartz job
 * being called evey 5 seconds (CRON 0/5 * * * * ?)
 * Frequency of the job is managed directly in DB (qrtz_cron_triggers)
 */
public class GeolocJob implements Job {

    private Logger logger = LoggerFactory.getLogger(GeolocJob.class);

    @Autowired
    private IGeolocService geolocService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("Début du Job de récupération des tickets de Geoloc...");
        geolocService.updateAwaitingGeolocs();
        logger.debug("Fin du Job.");
    }

}
