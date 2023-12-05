import React, { useEffect, useState, useLocation}from 'react';
import { useParams, useHistory} from 'react-router-dom';
import store from '../../component/catche/store';

import {
  Button,
  Card,
  CardActions,
  CardContent,
  Container,
  Grid,
  Typography,
  TextField
} from '@mui/material';
import './BookTicket.css'; // Import the CSS file


const mockEventData = {
  id: "",
  name: '',
  startdate: '',
  enddate: '',
  venue: {
    id: '',
    name: '',
    address: '',
    sections: [],
  },
  description: 'This is Event 3',
  artist: 'This is Event 3'
};

const BookTicket = () => {
  // const location = useLocation();
  const [selectedSection, setSelectedSection] = useState(null);
  const [numTickets, setNumTickets] = useState(1);
  const [eventData, setEventData] = useState(mockEventData);
	const {id} = useParams();
	const history = useHistory();
  const token = store.getToken();

  useEffect(() => {
    if (!id) {
      history.push('/');
    } else {
      // Fetch event data from the API
      fetch(`${process.env.REACT_APP_API_URL}/view-event?mode=one&id=${id}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`, // Include the token in the Authorization header
          'Content-Type': 'application/json',
        },
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            alert('Unauthorized Error ');
            throw new Error('Unauthorized');
          }
        })
        .then((data) => setEventData(data))
        .catch((error) => {
          console.error('Error fetching event data:', error);
        });
    }
  }, [history, id, token]);

  const handleSectionSelect = (section) => {
    setSelectedSection(section);
  };

	const handleNumTicketsChange = (event) => {
    setNumTickets(event.target.value);
  };

  const handleBookTickets = async () => {
    if (selectedSection !== null && numTickets > 0) {
        console.log({
            eventID: eventData.id,
            sectionID: selectedSection.id,
            numTickets: numTickets,
            version: selectedSection.version
        });

        fetch(
            `${process.env.REACT_APP_API_URL}/create-booking`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify({
                customerEmail: store.getEmail(),
                eventID: eventData.id,
                sectionID: selectedSection.id,
                numTickets: numTickets,
                version: selectedSection.version
            }),
        })
        .then(res => {
            if (!res.ok) { 
                // Check if response was successful
                return res.json().then(data => { 
                    // Use the JSON data to throw a more detailed error
                    throw new Error(`Error status: ${res.status}. Message: ${data.message || 'Unknown error'}`);
                });
            }

            console.log('Tickets locked successfully');
            alert('Tickets locked successfully');
            history.push(`/viewBookings/byCustomer/${store.getEmail()}`);
        })
        .catch(err => {
            // Instead of just logging, show a more user-friendly message
            alert("Unable to book the ticket");
            window.location.reload();
        });
    } else {
        alert("Select Section or number of Tickets");
    }
};

		
  return (
    <Container maxWidth="md" className='book-ticket-container'>
      {eventData && (
        <Card>
          <CardContent className='card-content'>
            <Typography variant="h4" gutterBottom>
              {eventData.name}
            </Typography>
            <Typography variant="body1" gutterBottom>
              Description: {eventData.description}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Date: {eventData.startDate} ~ {eventData.endDate}
              <br />
              Venue: {eventData.venue.name}
              <br />
              Address: {eventData.venue.address}
            </Typography>
            <Typography variant="h6" gutterBottom>
              Available Sections:
            </Typography>
            <Grid container spacing={2}>
            {eventData.venue.sections.map((section, index) => (
                <Grid item xs={12} sm={6} key={index}>
                    <Card variant="outlined" className='section-card'>
                        <CardContent>
                            <Typography variant="subtitle1">{section.type}</Typography>
                            <Typography variant="body2" color="text.secondary">
                                Price: ${section.price}
                                <br />
                                Tickets Left: {section.ticketLeft}
                            </Typography>
                        </CardContent>
                        <CardActions>
                            <Button
                                variant="contained"
                                onClick={() => handleSectionSelect(section)}
                                className='book-button'
                                disabled={section.ticketLeft === 0} // Disable button if ticketLeft is zero
                            >
                                Select
                            </Button>
                        </CardActions>
                    </Card>
                </Grid>
            ))}
            </Grid>
            {selectedSection !== null && (
              <Card className='selected-section'>
                <Typography variant="h6" gutterBottom className='section-info'>
                  Selected Section: {selectedSection.type}
                </Typography>
                <div className="num-tickets-input">
                  <TextField
                    label="Number of Tickets"
                    type="number"
                    value={numTickets}
                    onChange={handleNumTicketsChange}
                    className="num-tickets-input"
                    inputProps={{
                      min: 1,
                      max: selectedSection.ticketLeft,
                    }}
                    sx={{ mt: 2 }}
                  />
                </div>

                <Button
                  variant="contained"
                  color="primary"
                  className='book-button'
                  onClick={handleBookTickets}
                >
                  Book Tickets
                </Button>
              </Card>
            )}
          </CardContent>
        </Card>
      )}
    </Container>
  );
};

export default BookTicket;