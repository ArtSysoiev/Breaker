package com.example.breaker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.transition.TransitionManager;

import com.google.android.material.card.MaterialCardView;

public class SettingsActivity extends AppCompatActivity {
    String username, oldUsername;
    String theme;
    String language;


    TextView titleLabel;
    TextView usernameHeader;
    TextView themeHeader;
    TextView languageHeader;

    EditText usernameInput;
    ImageButton editUsernameButton;

    MaterialCardView cardLightTheme;
    MaterialCardView cardDarkTheme;

    MaterialCardView cardEnglish;
    MaterialCardView cardRussian;
    MaterialCardView cardUkrainian;
    MaterialCardView cardPolish;

    GridLayout languageLayout;
    GridLayout themeLayout;

    Button applyButton;

    boolean isEditingUsername = false;


    @Override
    public void finish() {
        if (Utils.isPreferencesSet(this)) {
            Utils.showToast(this, R.string.settings_saved_text);
        }
        super.finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setSelectedLanguage(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = Utils.getPreferencesUsernameOrDefault(this);
        theme = Utils.getPreferencesThemeOrDefault(this);

        language = Utils.getPreferencesLanguageOrDefault(this);


        titleLabel = findViewById(R.id.settings_title_label);
        usernameHeader = findViewById(R.id.username_header);
        themeHeader = findViewById(R.id.theme_header);
        languageHeader = findViewById(R.id.language_header);

        usernameInput = findViewById(R.id.username_input);
        editUsernameButton = findViewById(R.id.edit_username_button);

        cardLightTheme = findViewById(R.id.card_light_theme);
        cardDarkTheme = findViewById(R.id.card_dark_theme);

        cardEnglish = findViewById(R.id.card_english);
        cardRussian = findViewById(R.id.card_russian);
        cardUkrainian = findViewById(R.id.card_ukrainian);
        cardPolish = findViewById(R.id.card_polish);

        languageLayout = findViewById(R.id.language_layout);
        themeLayout = findViewById(R.id.theme_layout);

        applyButton = findViewById(R.id.apply_btn);

        usernameInput.setHint(username);
        usernameInput.setEnabled(false);

        cardLightTheme.setChecked(false);
        cardDarkTheme.setChecked(false);

        switch (theme) {
            case Config.preferencesThemeDark:
                cardDarkTheme.setChecked(true);
                break;
            case Config.preferencesThemeLight:
                cardLightTheme.setChecked(true);
                break;
        }

        switch (language) {
            case Config.preferencesLanguageEnglish:
                cardEnglish.setChecked(true);
                break;
            case Config.preferencesLanguageRussian:
                cardRussian.setChecked(true);
                break;
            case Config.preferencesLanguageUkrainian:
                cardUkrainian.setChecked(true);
                break;
            case Config.preferencesLanguagePolish:
                cardPolish.setChecked(true);
                break;
        }

        editUsernameButton.setOnClickListener(v -> {
            if (isEditingUsername) { //user wrote new username
                usernameInput.setEnabled(false);
                username = usernameInput.getText().toString();
                Utils.setPreference(this, Config.preferencesUsername, username);
                if (username.isBlank()) { // if user clear everything - user wants to reset username
                    username = getResources().getString(R.string.default_username);
                    Utils.clearPreference(this, Config.preferencesUsername);
                }
                usernameInput.setText("");
                usernameInput.setHint(username);
                editUsernameButton.setImageResource(R.drawable.icon_edit);
                isEditingUsername = false;
            } else {
                usernameInput.setEnabled(true);
                isEditingUsername = true;
                Utils.focusView(usernameInput);
                if (Utils.isUsernamePreferencesSet(this)) {
                    usernameInput.setText(username); // on username resetion we leave placeholder
                }
                editUsernameButton.setImageResource(R.drawable.icon_confirm);
            }
        });

        cardLightTheme.setOnClickListener(v -> {
            selectCard(cardLightTheme);
            theme = Config.preferencesThemeLight;
            Utils.setPreference(this, Config.preferencesTheme, theme);
            Utils.setLightTheme();
        });

        cardDarkTheme.setOnClickListener(v -> {
            selectCard(cardDarkTheme);
            theme = Config.preferencesThemeDark;
            Utils.setPreference(this, Config.preferencesTheme, theme);
            Utils.setDarkTheme();
        });

        cardEnglish.setOnClickListener(v -> {
            selectCard(cardEnglish);
            language = Config.preferencesLanguageEnglish;
            Utils.setPreference(this, Config.preferencesLanguage, language);
            Utils.setLanguage(this, Config.preferencesLanguageEnglish, true);
        });

        cardRussian.setOnClickListener(v -> {
            selectCard(cardRussian);
            language = Config.preferencesLanguageRussian;
            Utils.setPreference(this, Config.preferencesLanguage, language);
            Utils.setLanguage(this, Config.preferencesLanguageRussian, true);
        });

        cardUkrainian.setOnClickListener(v -> {
            selectCard(cardUkrainian);
            language = Config.preferencesLanguageUkrainian;
            Utils.setPreference(this, Config.preferencesLanguage, language);
            Utils.setLanguage(this, Config.preferencesLanguageUkrainian, true);
        });

        cardPolish.setOnClickListener(v -> {
            selectCard(cardPolish);
            language = Config.preferencesLanguagePolish;
            Utils.setPreference(this, Config.preferencesLanguage, language);
            Utils.setLanguage(this, Config.preferencesLanguagePolish, true);
        });

        applyButton.setOnClickListener(v -> {
//            Utils.setPreference(this, Config.preferencesUsername, username);
            Utils.setPreference(this, Config.preferencesTheme, theme);
            Utils.setPreference(this, Config.preferencesLanguage, language);

//            Utils.showToast(this, R.string.settings_saved_text);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void selectCard(MaterialCardView selectedCard) {
        GridLayout cardLayout = (GridLayout) selectedCard.getParent();

        if (selectedCard.isChecked()) {
            return;
        }

        TransitionManager.beginDelayedTransition(cardLayout);

        for (int i = 0; i < cardLayout.getChildCount(); i++) {
            MaterialCardView card = (MaterialCardView) cardLayout.getChildAt(i);
            if (card != selectedCard) {
                card.setChecked(false);
            }
        }

        selectedCard.setChecked(true);
    }
}