package fi.dy.masa.malilib.config.option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import fi.dy.masa.malilib.listener.EventListener;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.data.ModInfo;

public class BaseConfig extends CommonDescription implements ConfigInfo
{
    protected final List<String> searchStrings = new ArrayList<>(0);
    protected final List<String> oldNames = new ArrayList<>(0);
    protected boolean locked;
    @Nullable protected EventListener labelClickHandler;

    public BaseConfig(String name)
    {
        this(name, name, name);
    }

    public BaseConfig(String name, String commentTranslationKey, Object... commentArgs)
    {
        this(name, name, commentTranslationKey, commentArgs);
    }

    public BaseConfig(String name, String nameTranslationKey,
                      @Nullable String commentTranslationKey, Object... commentArgs)
    {
        super(name, nameTranslationKey, commentTranslationKey, commentArgs);
    }

    public void setOldNames(String... names)
    {
        this.oldNames.clear();
        this.oldNames.addAll(Arrays.asList(names));
    }

    @Override
    public List<String> getSearchStrings()
    {
        return this.searchStrings;
    }

    @Override
    @Nullable
    public EventListener getLabelClickHandler()
    {
        return this.labelClickHandler;
    }

    @Override
    public boolean isModified()
    {
        return false;
    }

    @Override
    public void resetToDefault()
    {
        // NO-OP
    }

    @Override
    public void setModInfo(ModInfo modInfo)
    {
        super.setModInfo(modInfo);

        String modId = modInfo.getModId();

        // If these are still using the default values, generate the proper keys
        if (this.nameTranslationKey.equals(this.name))
        {
            this.nameTranslationKey = this.createNameTranslationKey(modId);
        }

        if (this.commentTranslationKey != null && this.commentTranslationKey.equals(this.name))
        {
            this.commentTranslationKey = this.createCommentTranslationKey(modId);
        }

        if (this.searchStrings.isEmpty())
        {
            this.searchStrings.add(this.getDisplayName());
        }
    }

    public void setLabelClickHandler(@Nullable EventListener clickHandler)
    {
        this.labelClickHandler = clickHandler;
    }

    protected String createNameTranslationKey(String modId)
    {
        String nameLower = this.getName().toLowerCase(Locale.ROOT);
        return modId + ".config.name." + nameLower;
    }

    protected String createPrettyNameTranslationKey(String modId)
    {
        return this.createNameTranslationKey(modId);
    }

    protected String createCommentTranslationKey(String modId)
    {
        String nameLower = this.getName().toLowerCase(Locale.ROOT);
        return modId + ".config.comment." + nameLower;
    }

    /**
     * Adds additional search terms to this config.
     * By default the pretty name is used for searching against.
     */
    public BaseConfig addSearchTerms(Collection<String> searchTerms)
    {
        this.searchStrings.addAll(searchTerms);
        return this;
    }

    public static String getDefaultDisplayName(String baseName, String nameTranslationKey)
    {
        String translatedName = StringUtils.translate(nameTranslationKey);

        // If there is no translation for the config name, then show the actual base name
        return translatedName.equals(nameTranslationKey) ? baseName : translatedName;
    }
}
