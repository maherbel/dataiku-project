import React, { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import styled from 'styled-components';

const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 8px;
`;

const ButtonContainer = styled.div`
    display: flex;
    margin-top: 16px;
`;

const Title = styled.h1`
    font-weight: 500;
    color: ${({ theme }) => theme.primary};
`;

const FileInput = styled.input`
    display: none
`;

const UploadButton = styled.button`
    margin-right: 16px;
`;

const Result = styled.p`
    margin-top: 16px;
    font-size: 1.5rem;
    font-weight: bold;
    color: ${({ theme }) => theme.secondary};
`;



const App = () => {
  const [result, setResult] = useState(null);
  const [jsonFile, setJsonFile] = useState(null);
  const fileInputRef = useRef(null);

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        try {
          const data = JSON.parse(e.target.result);
          setJsonFile(data);
        } catch (error) {
          console.error(error);
        }
      };
      reader.readAsText(file);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      if (jsonFile) {
        try {
          const response = await axios.post('/missionResultSuccess', jsonFile);
          setResult(response.data);
        } catch (error) {
          console.error(error);
        }
      }
    };
    fetchData();
  }, [jsonFile]);

  const handleUploadClick = (event) => {
    event.preventDefault();
    fileInputRef.current.click();
  };

  return (
    <Container maxWidth="sm">
      <Title>
        Upload the Empire Intercepted data
      </Title>
      <form>
        <ButtonContainer>
          <UploadButton variant="contained" color="primary" onClick={handleUploadClick} type='button' data-testid="upload-button">
            Upload
          </UploadButton>
          <FileInput
            accept=".json"
            id="upload-file"
            data-testid="upload-file"
            type="file"
            ref={fileInputRef}
            onChange={handleFileChange}
          />
        </ButtonContainer>
      </form>
      <Result data-testid="result">
        Success Probability: {result ? (result.missionSuccessProbability + '%') : 'n/a'}
      </Result>
    </Container>
  );
}

export default App;