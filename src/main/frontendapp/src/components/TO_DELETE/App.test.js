import '@testing-library/jest-dom'
import { render, fireEvent } from '@testing-library/react';
import axios from 'axios';
import App from './App';

jest.mock('axios')

describe('App component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  test('Renders the component', async () => {
    const { getByText } = render(<App />);
    expect(getByText(/Upload the Empire Intercepted data/i)).toBeInTheDocument();
  });

  test('renders upload button correctly', () => {
    const { getByText } = render(<App />);
    const uploadButton = getByText('Upload');
    expect(uploadButton).toBeInTheDocument();
  });

  test('Test file input change', async () => {
    const { getByTestId } = render(<App />);
    const fileInput = getByTestId("upload-file");
    const file = new File(["{}"], "mock.json", { type: "application/json" });
    Object.defineProperty(fileInput, 'files', { value: [file] });
    fireEvent.change(fileInput);
    expect(fileInput.files).toEqual([file]);
  });

  test("Test that the handleFileChange function is called when the Upload button is clicked:", () => {
    const { getByTestId } = render(<App />);
    const uploadButton = getByTestId("upload-button");
    const handleUploadClick = jest.fn();
    uploadButton.onClick = handleUploadClick();
    fireEvent.click(uploadButton);
    expect(handleUploadClick).toHaveBeenCalled();
  });

  test("Test that result state is set when setResult is called", async () => {
    jest.spyOn(axios, "post").mockResolvedValue({data:{missionSuccessProbability:"90"}});
    
    // Render App
    const { getByTestId, findByText } = render(<App />);

    // Simulate a file being selected
    const fileInput = getByTestId("upload-file");
    fireEvent.change(fileInput, { target: { files: [new File(["{}"], "mock.json", { type: "application/json" })] } });

    // Assert that the state of result has been updated correctly
    const result = await findByText("Success Probability: 90%");
    expect(result).toBeInTheDocument();
  });

  test.skip("Test that console.error is called on error", () => {
    const consoleErrorSpy = jest.spyOn(console, "error").mockImplementation(() => {});
    axios.post.mockRejectedValue(new Error("Mock Error"));
  
    const { getByTestId } = render(<App />);
    const fileInput = getByTestId("upload-file");
    fireEvent.change(fileInput, { target: { files: [new File(["{}"], "mock.json", { type: "application/json" })] } });
  
    expect(consoleErrorSpy).toHaveBeenCalled();
    consoleErrorSpy.mockRestore();
  });

})