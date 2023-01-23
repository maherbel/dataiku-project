import React from "react";
import { AppBar, Box, Toolbar, Typography } from "@mui/material";


const Footer = () => {
  return (
    <Box>
      <AppBar position="static" sx={{
        bottom: 0,
        width: '100%',
        height: '100%',
      }}>
        <Toolbar>
          <Typography variant="body2">Copyright © Dataiku Technical Project</Typography>
        </Toolbar>
      </AppBar>
    </Box>
  )
};

export default Footer;