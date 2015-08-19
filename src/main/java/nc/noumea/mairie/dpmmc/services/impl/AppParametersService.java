package nc.noumea.mairie.dpmmc.services.impl;

import nc.noumea.mairie.dpmmc.dao.AppParametersDao;
import nc.noumea.mairie.dpmmc.services.interfaces.IAppParametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppParametersService implements IAppParametersService {

    @Autowired
    AppParametersDao appParametersDao;

    @Override
    public int getQueryMaxResults() {
        return appParametersDao.getParameter("nb_max_results");
    }

    @Override
    public int getTablePageSize() {
        return appParametersDao.getParameter("nb_max_results_page");
    }

    @Override
    public int getActivitesTablePageSize() {
        return appParametersDao.getParameter("nb_max_fiches_page");
    }
}
