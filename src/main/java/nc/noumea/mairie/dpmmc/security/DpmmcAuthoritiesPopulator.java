package nc.noumea.mairie.dpmmc.security;

import nc.noumea.mairie.dpmmc.domain.Profil;
import nc.noumea.mairie.dpmmc.domain.RolesEnum;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Overriden Authorities Populator for Ldap authentication with spring security
 * This class loads roles from the database i/o using LDAP groups
 */
@Service
public class DpmmcAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    @PersistenceContext(unitName = "dpmmcPersistenceUnit")
    private EntityManager _entityManager;

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations dirContextOperations, String s) {

        TypedQuery<Profil> queryP = _entityManager.createQuery("select profil from Utilisateur where identifiant = :username", Profil.class);
        queryP.setParameter("username", s);
        List<Profil> ps = queryP.getResultList();

        List<GrantedAuthority> roles = new ArrayList<>();

        if (ps.isEmpty()) {
            return roles;
        }

        Profil p  = ps.get(0);

        roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_USER.toString()));

        if (p.isDroitAdministration())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_ADMINISTRATION.toString()));

        if (p.isDroitCreationFiche())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_CREATION_FICHE.toString()));

        if (p.isDroitModificationFicheToutesBrigades())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_MODIF_FICHE_N_BRIGADES.toString()));

        if (p.isDroitModificationFicheLaBrigade())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_MODIF_FICHE_1_BRIGADE.toString()));

        if (p.isDroitClotureFiche())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_CLOTURE_FICHE.toString()));

        if (p.isDroitDeclotureFiche())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_DECLOTURE_FICHE.toString()));

        if (p.isDroitRechercheFiche())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_RECH_FICHE.toString()));

        if (p.isDroitSuppressionFiche())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_SUPPR_FICHE.toString()));

        if (p.isDroitStatistiques())
            roles.add(new SimpleGrantedAuthority(RolesEnum.ROLE_STATS.toString()));

        return roles;
    }

}
