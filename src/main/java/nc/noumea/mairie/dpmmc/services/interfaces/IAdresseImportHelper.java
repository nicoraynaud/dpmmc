package nc.noumea.mairie.dpmmc.services.interfaces;

import org.springframework.security.access.annotation.Secured;
import org.zkoss.util.media.Media;

import java.util.List;

public interface IAdresseImportHelper {

    @Secured({"ROLE_ADMINISTRATION"})
    List<String> importAddressFromFile(Media file);
}
