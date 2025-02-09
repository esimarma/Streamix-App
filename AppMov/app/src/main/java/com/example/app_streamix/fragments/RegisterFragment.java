package com.example.app_streamix.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_streamix.R;
import com.example.app_streamix.models.RegisterRequest;
import com.example.app_streamix.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private AuthRepository authRepository;

    public RegisterFragment() {
        // Construtor vazio necessário
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Inicializar o repositório
        authRepository = new AuthRepository();

        // Inicializar Views
        Button switchToLoginButton = view.findViewById(R.id.switchToLoginButton);
        Button registerButton = view.findViewById(R.id.registerButton);
        ImageView backButton = view.findViewById(R.id.back_button);
        EditText passwordField = view.findViewById(R.id.passwordFieldRegister);
        EditText confirmPasswordField = view.findViewById(R.id.confirmPasswordField);
        EditText nameField = view.findViewById(R.id.nameFieldRegister);
        EditText emailField = view.findViewById(R.id.emailFieldRegister);
        CheckBox showPasswordCheckbox = view.findViewById(R.id.show_password);

        // Mostrar ou ocultar senha
        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                confirmPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        // Navegar para o LoginFragment
        switchToLoginButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Botão de voltar - vai para o ProfileFragment
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ProfileFragment())
                    .commit();
        });

        // Lógica de registro
        registerButton.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (validateFields(name, email, password, confirmPassword)) {
                registerUser(name, email, password);
            }
        });

        return view;
    }

    private boolean validateFields(String name, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Por favor, insira o seu nome.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Por favor, insira o seu e-mail.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Por favor, insira uma senha.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUser(String name, String email, String password) {
        RegisterRequest registerRequest = new RegisterRequest(name, email, password);

        authRepository.register(registerRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();

                    // Redirecionar para o LoginFragment após o registro
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Erro ao criar a conta. Verifique os dados.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Erro de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}