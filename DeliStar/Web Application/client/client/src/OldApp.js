import React from 'react'
import {useState, useEffect} from 'react'
import Axios from "axios"

function App() {
  const [UsersList, setUsersList] = useState([])
  const [UserName, setUserName] = useState([])
  const [Age, setAge] = useState([])
  const [Name, setName] = useState([])

  useEffect(() => {
    Axios.get("http://localhost:3007/getUsers")
    .then((response)=>{
      setUsersList(response.data)
    })
  }, []);

  const CreateUser = () =>{
    Axios.post("http://localhost:3007/createUser",
    {name:Name, age:Age, username:UserName})
    .then((res)=>{
      setUsersList([...UsersList,{name: UserName, username: UserName, age: Age}])
    })
  }

  return (
    <div className='App'>
      <div className="usersDisplay">
        <div>
          <input type="text" placeholder='name' onChange={(e)=>setName(e.target.value)} />
          <br/>
          <input type="number" placeholder='age' onChange={(e)=>setAge(e.target.value)} />
          <br/>
          <input type="text" placeholder='username' onChange={(e)=>setUserName(e.target.value)} />
          <br/>
          <button onClick={CreateUser}>Create User</button>
          <br/>
        </div>
        <hr/>
        <br/>
        {
          UsersList.map((user)=>{
            return(
              <div>
                <div>
                  <h1>Name: {user.name} </h1>
                  <h1>age: {user.age} </h1>
                  <h1>Username: {user.username} </h1>
                  <br/>
                </div>
              </div>
            )
          })
        }
      </div>
    </div>
  )
}

export default App
