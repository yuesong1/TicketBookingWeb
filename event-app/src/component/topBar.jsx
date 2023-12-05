import React, { useEffect, useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import AdbIcon from '@mui/icons-material/Adb';
import HeadphonesIcon from '@mui/icons-material/Headphones';
import { Link, useHistory } from 'react-router-dom';
import store from './catche/store';

const role_pages = {
  admin: ['Create Venue', 'View Venues', 'View Users'],
  planner: ['Create Event'],
  customer: ['Search Events', "Event Calendar"],
  user: ['Log In', 'Sign Up']
};

function ResponsiveAppBar() {
  const [anchorElNav, setAnchorElNav] = useState(null);
  const [anchorElUser, setAnchorElUser] = useState(null);
  const [role, setRole] = useState("user");

  const history = useHistory();
  const token = store.getToken();

  useEffect(() => setRole(store.getRole()), [store]);

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = (page) => {
    history.push(`/${page.replace(/\s+/g, '')}`);
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const handleLogout = async()=> {
    try {
      const response = await fetch(
        `${process.env.REACT_APP_API_URL}/logout`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({}),
      });

      if (response.status === 200) {
      // Clear token and role from local storage
      store.removeEmail();
      store.removeToken();
      store.removeRole();
      // Redirect to the login page
      history.push('/Login');
      }
    } catch (error) {
      console.error('Error during logout:', error);


      // // Clear token and role from local storage
      // store.removeEmail();
      // store.removeToken();
      // store.removeRole();
      
      // // Redirect to the login page
      // history.push('/Login');
    }
    window.location.reload();
  };

  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <HeadphonesIcon
            sx={{ display: { xs: 'none', md: 'flex' }, mr: 1 }}
          />
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            MusicBandTeam
          </Typography>

          <Box
            sx={{
              flexGrow: 1,
              display: { xs: 'flex', md: 'none' },
            }}
          >
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={() => setAnchorElNav(null)}
              sx={{
                display: { xs: 'block', md: 'none' },
              }}
            >
              {role_pages[role].map((page) => (
                <MenuItem
                  key={page}
                  onClick={() => handleCloseNavMenu(page)}
                >
                  <Typography textAlign="center">{page}</Typography>
                </MenuItem>
              ))}
              <MenuItem
                key={"View Events"}
                onClick={() => history.push("viewEvents/all")}
              >
                <Typography textAlign="center">View Events</Typography>
              </MenuItem>
            </Menu>
          </Box>
          <AdbIcon
            sx={{
              display: { xs: 'flex', md: 'none' },
              mr: 1,
            }}
          />
          <Typography
            variant="h5"
            noWrap
            component="a"
            href="/"
            sx={{
              mr: 2,
              display: { xs: 'flex', md: 'none' },
              flexGrow: 1,
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            LOGO
          </Typography>
          <Box
            sx={{
              flexGrow: 1,
              display: { xs: 'flex', md: 'flex' },
            }}
          >
            {role_pages[role].map((page) => (
              <Link
                key={page}
                to={`/${page.replace(/\s+/g, '')}`}
                style={{ textDecoration: 'none' }}
              >
                <Button
                  sx={{
                    my: 1,
                    color: 'white',
                    display: 'block',
                    width: '100%',
                  }}
                >
                  {page}
                </Button>
              </Link>
            ))}
            {role === "admin" &&
              <Link key="View Events" to={`/viewEvents/all`} style={{ textDecoration: 'none' }}>
                <Button sx={{ my: 1, color: 'white', display: 'block', width: '100%', }} >
                  View Events
                </Button>
              </Link>
            }
            {role === "planner" &&
              <Link key="View Events" to={`/viewEvents/byPlanner/${store.getEmail()}`} style={{ textDecoration: 'none' }}>
                <Button sx={{ my: 1, color: 'white', display: 'block', width: '100%', }} >
                  View Events
                </Button>
              </Link>
            }
            {
              role === "customer" &&
              <>
                <Link key="View Bookings" to={`/viewBookings/byCustomer/${store.getEmail()}`} style={{ textDecoration: 'none' }}>
                  <Button sx={{ my: 1, color: 'white', display: 'block', width: '100%', }} >
                    View Bookings
                  </Button>
                </Link>
              </>
            }
          </Box>

          <Box sx={{ flexGrow: 0 }}>
          <Tooltip title="Open settings">
              <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                <Avatar alt="Remy Sharp" src="/static/images/avatar/2.jpg" />
              </IconButton>
            </Tooltip>
            <Menu
  sx={{ mt: '45px' }}
  id="menu-appbar"
  anchorEl={anchorElUser}
  anchorOrigin={{
    vertical: 'top',
    horizontal: 'right',
  }}
  keepMounted
  transformOrigin={{
    vertical: 'top',
    horizontal: 'right',
  }}
  open={Boolean(anchorElUser)}
  onClose={handleCloseUserMenu}
>
  {token ? (
    // If the user is logged in, show Logout
    [<MenuItem key="logout" onClick={handleLogout}>
      <Typography textAlign="center">Logout</Typography>
    </MenuItem>]
  ) : (
    // If the user is not logged in, show Signup and Login
    [
      <MenuItem key="signup" onClick={() => history.push('/SignUp')}>
        <Typography textAlign="center">SignUp</Typography>
      </MenuItem>,
      <MenuItem key="login" onClick={() => history.push('/Login')}>
        <Typography textAlign="center">Login</Typography>
      </MenuItem>,
    ]
  )}
</Menu>

          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}

export default ResponsiveAppBar;

