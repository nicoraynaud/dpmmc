package nc.noumea.mairie.dpmmc.viewModel;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zul.SimpleListModel;

import java.lang.reflect.Field;
import java.util.List;

public class ContainsSimpleListModel<T> extends SimpleListModel {

    protected String property;
    protected Class clazz;
    protected Field field;


    public ContainsSimpleListModel(Object[] data, boolean live, Class clazz, String property) {
        super(data, live);
        initFilter(clazz, property);
    }

    public ContainsSimpleListModel(Object[] data, Class clazz, String property) {
        super(data);
        initFilter(clazz, property);
    }

    public ContainsSimpleListModel(List data, Class clazz, String property) {
        super(data);
        initFilter(clazz, property);
    }

    public void initFilter(Class clazz, String property) {
        this.clazz = clazz;
        this.property = property;

        try {
            this.field = clazz.getDeclaredField(property);
        } catch (NoSuchFieldException e) {
            try {
                this.field = clazz.getSuperclass().getDeclaredField(property);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }

        field.setAccessible(true);
    }

    @Override
    public boolean inSubModel(Object key, Object value) {
        String searchString = (String)key;
        if (StringUtils.isEmpty(searchString))
            return true;
        try {
            return StringUtils.containsIgnoreCase(field.get(value).toString(), searchString);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected int getMaxNumberInSubModel(int nRows) {
        return nRows < 0?1000:nRows;
    }
}
