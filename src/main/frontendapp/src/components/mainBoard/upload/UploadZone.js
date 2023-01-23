import { Typography, Box } from "@mui/material";
import { useState } from "react";
import { FileUploader } from "react-drag-drop-files";
import { selectedEmpireDataFileVar } from '../../../reactivities/reactiveVariables';
import ComponentsUtils from "../../../utils/ComponentsUtils";

import "./UploadZone.css";

const fileTypes = ["JSON"];

const UploadZone = () => {
  const [file, setFile] = useState(null);

  const handleFileChange = (file) => {
    if (file) {
      setFile(file);
      ComponentsUtils.toggleLoadingOverlay(true);
      const reader = new FileReader();
      reader.onload = (e) => {
        try {
          const data = JSON.parse(e.target.result);
          selectedEmpireDataFileVar({
            jsonContent: data,
            fileName: file.name
          });
          ComponentsUtils.toggleLoadingOverlay(false);
          ComponentsUtils.displayToastMessage(true, "success", "File " + file.name + " uploaded successfully !");
        } catch (error) {
          console.error(error);
          ComponentsUtils.toggleLoadingOverlay(false);
          ComponentsUtils.displayToastMessage(true, "error", "An error happened during the file upload ! Please try another file or contact support.");
        }
      };
      reader.readAsText(file);
    }
  };
  return (
    <Box>
      <FileUploader
        handleChange={handleFileChange}
        name="file"
        types={fileTypes}
        hoverTitle="Drop Here"
        classes="drop_zone"
        label="Please upload or drop here the Empire's intercepted data."
      />
      <Typography variant="body" sx={{ textAlign: "left", fontWeight: "bold" }}>
        {file ? `Uploaded file name: ${file.name}.` : ""}
      </Typography>
    </Box>
  );
}

export default UploadZone;
