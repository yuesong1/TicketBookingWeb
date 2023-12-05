import React, { useState, useEffect  } from 'react';
import {
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  TextField,
} from '@mui/material';
import { Link, useHistory } from 'react-router-dom';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import './CreateEvent.css'
import store from '../../component/catche/store';
import formatDateToCustomFormat from '../../component/helper/Localdate';


function CreateEvent() {
  const [selectedVenue, setSelectedVenue] = useState({});
  const [eventSections, setEventSections] = useState([]);
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedEndDate, setSelectedEndDate] = useState(null);
  const [eventName, setEventName] = useState('');
  const [eventArtist, setEventArtist] = useState('');
  const [eventDescription, setEventDescription] = useState('');
  const [venues, setVenues] = useState([]);
  const history = useHistory();
  const token = store.getToken();
  const myemail = store.getEmail();

  const [eventPlanners, setEventPlanners] = useState('');

  const handleEventPlannersChange = (event) => {
    setEventPlanners(event.target.value);
  };


  useEffect(() => {
    // Fetch venues data from the API with the token in the header
    fetch(
      `${process.env.REACT_APP_API_URL}/view-venue?mode=all`, 
    {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          alert('Unauthorized')
          throw new Error('Unauthorized');
        }
      })
      .then((data) => {
        setVenues(data);
        console.log(data)
      })
      .catch((error) => console.error('Error fetching venues:', error));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!selectedDate || !selectedEndDate) {
      console.error('Selected date or end date is invalid.');
      alert('Selected date or end date is invalid.')
      return; // Don't proceed further if dates are invalid
    }
  
    // Continue with date formatting and API call
    const formattedStartDate = formatDateToCustomFormat(selectedDate);
    const formattedEndDate = formatDateToCustomFormat(selectedEndDate);
    const plannerEmails = eventPlanners.split(',').map((email) => email.trim());
    plannerEmails.push(myemail);

    const validPlannerEmails = plannerEmails.filter(isValidEmail);

    try {
      // Prepare data for submission
      var eventDetails = {
        name: eventName,
        artist: eventArtist,
        description: eventDescription,
        planners: validPlannerEmails,
        version:0,
        venue: {...selectedVenue, sections: selectedVenue.sections.map(s => {
          Object.keys(eventSections).forEach(esType => {
            if (s.type === esType) {
              s["price"] = eventSections[esType];
              s["avalbility"] = s.capacity;
            }
          });
          return s;
        })},
        
        startdate: formattedStartDate, // Convert to ISO string format
        enddate: formattedEndDate,
      };
      console.log(eventDetails);
      // Send the data to your API endpoint
      const response = await fetch(`${process.env.REACT_APP_API_URL}/create-event`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(eventDetails),
      });
      if (response.status === 201) {
        console.log('Event created successfully');
        alert('Event created successfully');
        history.push(`/viewEvents/byPlanner/${store.getEmail()}`);
    } else {
        const errorData = await response.json();
        let errorMessage;

        switch(response.status) {
            case 405:
                errorMessage = 'Concurrent update detected. Please try again.';
                break;
            case 400:
                errorMessage = errorData.message || 'Unknown error during event creation.';
                break;
            default:
                errorMessage = `Unexpected error. Status code: ${response.status}. Message: ${errorData.message || 'Unknown error'}`;
        }

        throw new Error(errorMessage);
    }
      
      } catch (error) {
          console.error('An error occurred:', error);
          alert(error.message);
      }

        };
  
  const handleVenueChange = (event) => {
    const venueId = event.target.value;
    const venue = venues.filter(v => v.id === venueId)[0];
    setSelectedVenue(venue);

    // Reset ticket prices for each section
    setEventSections({});
  };

  const handleTicketPriceChange = (section, price) => {
    setEventSections((es) => {
      es[section.type] = parseInt(price, 10)
      return es;
    });
  };
  
  function isValidEmail(email) {
    // Regular expression for simple email validation
    const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return regex.test(email);
}

  
  const handleEventNameChange = (event) => {
    setEventName(event.target.value);
  };

  
  const handleDateChange = (date) => {
    setSelectedDate(date.toISOString());
    console.log(selectedDate);
  };

  const handleEndDateChange = (date) => {
    setSelectedEndDate(date.toISOString());
    console.log(selectedEndDate);
  };

  const handleEventArtistChange = (event) => {
    setEventArtist(event.target.value);
  };

  const handleEventDescriptionChange = (event) => {
    setEventDescription(event.target.value);
  };

  return (
    <div className="create-event-container">
      <h2>Create Project</h2>

      <div className="event-details-container">
        <TextField
          label="Event Name"
          value={eventName}
          onChange={handleEventNameChange}
          fullWidth
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Event Artist"
          value={eventArtist}
          onChange={handleEventArtistChange}
          fullWidth
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Event Description"
          value={eventDescription}
          onChange={handleEventDescriptionChange}
          fullWidth
          multiline
          rows={4}
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Other Event Planners (comma-separated emails)"
          value={eventPlanners}
          onChange={handleEventPlannersChange}
          fullWidth
          sx={{ marginBottom: 2 }}
        />

      </div>

      <FormControl sx={{ minWidth: 200, marginBottom: 2 }} className="form-control">
        <InputLabel id="venue-select-label">Select Venue</InputLabel>
        <Select
          labelId="venue-select-label"
          id="venue-select"
          value={selectedVenue.id}
          label="Select Venue"
          onChange={handleVenueChange}
        >
          {venues.map((venue, index) => (
            <MenuItem key={index} value={venue.id}>
              {venue.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      {selectedVenue.id !== undefined && (
        <div className="ticket-section-container">
          <h3>Set Ticket Prices</h3>
          {selectedVenue.sections.map((section) => (
            <div key={section.type} className="ticket-section">
              <div className="ticket-line">
                <span className="ticket-label">Price of {section.type}:</span>
                <TextField
                  value={eventSections[section.type]}
                  type="number"
                  onChange={(e) =>
                    handleTicketPriceChange(section, e.target.value)
                  }
                  className="ticket-price-input"
                />
              </div>
            </div>
          ))}
        </div>
      )}


<h3>Choose Date and time </h3>

  <div className="date-picker-container">

    <LocalizationProvider dateAdapter={AdapterDayjs}>
      Start Time: 
        <DateTimePicker
          label="Start Time"
          value={selectedDate}
          onChange={handleDateChange}
        />

        End Time:
        <DateTimePicker
          label="End Time"
          value={selectedEndDate}
          onChange={handleEndDateChange}
        />
    </LocalizationProvider>
  </div>

    
    <div className='button-container'>
    <Button variant="contained" color="primary" onClick={handleSubmit}>
        Create Project
      </Button>
      <Link to="/ViewEvents">
        <Button>Back to Events</Button>
      </Link>
    </div>
     
    </div>
  );
}

export default CreateEvent;