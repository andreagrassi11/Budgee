package com.example.examproject.fragments.insert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.examproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

public class InsertReceiptFragment extends Fragment {
    private ImageView imageView;
    private Button recognizeButton;
    private Bitmap selectedImage;

    // Register activity result launchers for selecting/capturing images
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                        imageView.setImageBitmap(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    selectedImage = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(selectedImage);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_receipt, container, false);
        imageView = view.findViewById(R.id.imageView);
        Button selectButton = view.findViewById(R.id.selectButton);
        Button captureButton = view.findViewById(R.id.captureButton);
        recognizeButton = view.findViewById(R.id.recognizeButton);

        selectButton.setOnClickListener(v -> selectImage());
        captureButton.setOnClickListener(v -> captureImage());
        recognizeButton.setOnClickListener(v -> {
            if (selectedImage != null) {
                runTextRecognition();

            } else
                Toast.makeText(getContext(), "Select an image", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    // Open gallery to select an image
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    // Open camera to capture an image
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    // Process text recognition
    private void runTextRecognition() {

        InputImage image = InputImage.fromBitmap(selectedImage, 0);
        TextRecognizer recognizer = TextRecognition.getClient();
        recognizeButton.setEnabled(false);

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                List<String> value = processTextRecognitionResult(visionText);
                                viewModel.setRecognizedTextList(value);

                                InsertAIFragment fragmentB = new InsertAIFragment();

                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragmentContainerView, fragmentB)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
    }

    private List<String> processTextRecognitionResult(Text texts) {
        StringBuilder recognizedText = new StringBuilder();

        // Build the full recognized text from the OCR result
        for (Text.TextBlock block : texts.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                for (Text.Element element : line.getElements()) {
                    recognizedText.append(element.getText());
                }
                recognizedText.append("\n");
            }
        }

        String fullText = recognizedText.toString();

        String name = extractName(fullText);
        String date = extractDate(fullText);
        double total = extractTotal(fullText);

        List<String> receiptDetails = new ArrayList<>();
        receiptDetails.add(name);
        receiptDetails.add(date);
        receiptDetails.add(String.valueOf(total));

        return receiptDetails;
    }

    private String extractName(String text) {
        // Split the text into lines and get the first line as the name
        String[] lines = text.split("\n");
        return lines.length > 0 ? lines[0].trim() : "Unknown Name";
    }

    private String extractDate(String text) {
        Pattern pattern = Pattern.compile("\\b(\\d{2}[-/]\\d{2}[-/]\\d{2,4})\\d{0,2}:?\\d{0,2}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "Unknown Date";
        }
    }

    private double extractTotal(String text) {
        // Regex pattern for numeric values (handles both integer and decimal numbers with commas)
        Pattern pattern = Pattern.compile("\\b(\\d{1,3}(?:,\\d{3})*(?:,\\d{2}))\\b");
        Matcher matcher = pattern.matcher(text);

        double maxValue = -1;

        // Iterate over the numbers found and get the largest one
        while (matcher.find()) {
            String numberStr = matcher.group(1).replace(",", ".");  // Replace comma with dot for decimal
            try {
                double number = Double.parseDouble(numberStr);
                maxValue = Math.max(maxValue, number);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return maxValue > -1 ? maxValue : 0.0;  // Return the max value found, or 0 if no numbers found
    }
}