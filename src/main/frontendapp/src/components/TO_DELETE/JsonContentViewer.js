// import the react-json-view component
import { useReactiveVar } from '@apollo/client';
import { Box } from '@mui/system';
import ReactJson from 'react-json-view'
import { selectedEmpireDataFileVar } from '../../reactivities/reactiveVariables';

const JsonContentViewer = () => {
    const { jsonContent } = useReactiveVar(selectedEmpireDataFileVar);

    if (jsonContent) {
        return (
            <Box sx={{ border: 1, overflow: 'auto' }}>
                <ReactJson src={jsonContent} enableClipboard={false} />
            </Box>
        );
    }
}

export default JsonContentViewer;