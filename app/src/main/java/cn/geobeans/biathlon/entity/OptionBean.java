package cn.geobeans.biathlon.entity;

import com.contrarywind.interfaces.IPickerViewData;

public class OptionBean implements IPickerViewData {
    String name;

    public OptionBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
