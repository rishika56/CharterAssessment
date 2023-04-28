import './App.css';
import purchaseData from './dataset/purchase';
import { useEffect, useState } from 'react';
import * as moment from 'moment'
import { Multiselect } from 'multiselect-react-dropdown';


const App = () => {

  const [data, setData] = useState(purchaseData.data);
  const [date, setDate] = useState('');
  const [purchaseAmount, setPurchaseAmount] = useState(0);
  const [monthRewards, setMonthReward] = useState([[1, 0], [2, 0], [3, 0], [4, 0], [5, 0], [6, 0], [7, 0], [8, 0], [9, 0], [10, 0], [11, 0], [12, 0]]);
  const [filterWithMonths] = useState([{ name: 'Jan', id: 1 }, { name: 'Feb', id: 2 }, { name: 'Mar', id: 3 }, { name: 'April', id: 4 }, { name: 'May', id: 5 }, { name: 'Jun', id: 6 }, { name: 'Jul', id: 7 }, { name: 'Aug', id: 8 }, { name: 'Sept', id: 9 }, { name: 'Oct', id: 10 }, { name: 'Nov', id: 11 }, { name: 'Dec', id: 12 }])
  const [selectedValue] = useState([]);
  const [montwiseRewardValue, setMontwiseRewardValue] = useState(0)
  useEffect(() => {
    calculateReward();
  }, [])

  const save = () => {
    if (date && purchaseAmount && purchaseAmount != "") {
      let tempData = [...data];
      tempData.push({ date: new Date(date), purchaseAmount: purchaseAmount })
      setData(tempData)
      console.log(data);
      calculateReward()
    }
  }

  const calculateReward = () => {
    let arr = [[1, 0], [2, 0], [3, 0], [4, 0], [5, 0], [6, 0], [7, 0], [8, 0], [9, 0], [10, 0], [11, 0], [12, 0]]
    data.forEach(element => {
      let month = moment(element.date).format('M');
      let index = arr.findIndex(x => x[0] == month);
      arr[index][1] = arr[index][1] + calculateRewards(element.purchaseAmount)
      console.log(month)
    })
    setMonthReward(arr);
    console.log(arr)
  }

  function calculateRewards(price) {
    if (price >= 50 && price < 100) {
      return price - 50;
    } else if (price > 100) {
      return (2 * (price - 100) + 50);
    }
    return 0;
  }

  function onSelect(selectedList, selectedItem) {
    console.log(selectedList, selectedItem);
    calculateSelectedMonthReward(selectedList)
  }

  function onRemove(selectedList, removedItem) {
    console.log(selectedList, removedItem);
    calculateSelectedMonthReward(selectedList)
    
  }

  function calculateSelectedMonthReward(selectedList){
        let value = 0;
        selectedList.forEach(element =>{
            value = value + monthRewards[element.id -1][1]
        })
        console.log(value);
        setMontwiseRewardValue(value);
  }

  return (
    <div className="App">
      <header className="App-header">
        <div class="margin-wise">
          Add new purchase :
           <div>
            <label>Amount :</label>
            <input type="number" min="1" value={purchaseAmount} onChange={(event) => {
              console.log(event.target.value);
              setPurchaseAmount(event.target.value)
            }} ></input>
          </div>
          <div>
            <label>Date :</label>
            <input type="date" value={date} onChange={(event) => {
              console.log(event.target.value);
              setDate(event.target.value)
            }} min="1" ></input>
          </div>
          <div>
            <button onClick={() => save()}>Add</button>
          </div>
        </div>
          <div class="margin-wise">
        <div>
          <div  >
            <table id="customers">
              <thead></thead>
              <tbody>

                <tr>
                  <th>Sr.No</th>
                  <th>Date</th>
                  <th>Purchase Amount</th>
                </tr>
                {data.map((value, index) => {
                  return <tr key={index}>
                    <td>{index}</td>
                    <td>{moment(value.date).format("DD-MM-YYYY")}</td>
                    <td>{value.purchaseAmount}</td>
                  </tr>
                })}
              </tbody>

            </table>
          </div>
        </div>
        </div>

        <div class="margin-wise">
          Get Selected Month Rewards:

<Multiselect
            options={filterWithMonths} // Options to display in the dropdown
            selectedValues={selectedValue} // Preselected value to persist in dropdown
            onSelect={onSelect} // Function will trigger on select event
            onRemove={onRemove} // Function will trigger on remove event
            displayValue="name" // Property name to display in the dropdown options
          />
       
        <div>
          {montwiseRewardValue}
        </div>
        </div>


        <div class="margin-wise">
          Monthly Rewards
          <table id="customers">
            <thead></thead>
            <tbody>
              <tr>
                <th>Month</th>
                <th>Reward</th>
              </tr>
              {monthRewards.map((value, index) => {
                return <tr>
                  <td>
                    {value[0]}
                  </td>
                  <td>
                    {value[1]}
                  </td>
                </tr>
              })
              }
            </tbody>
          </table>
        </div>


      </header>
    </div>
  );
}

export default App;
