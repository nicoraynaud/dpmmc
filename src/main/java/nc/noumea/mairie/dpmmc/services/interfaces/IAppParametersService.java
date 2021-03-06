package nc.noumea.mairie.dpmmc.services.interfaces;

public interface IAppParametersService {

    int getQueryMaxResults();

    int getTablePageSize();

    int getActivitesTablePageSize();

    int getTicketsRefreshInterval();

    int getMaxTicketsNb();
}
