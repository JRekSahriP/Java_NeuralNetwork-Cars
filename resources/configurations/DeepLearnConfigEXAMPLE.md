## Configuration Options
| Option                           | Type       | Description                                     						|
|----------------------------------|------------|-----------------------------------------------------------------------|
| `fps_limit`                      | double     | Maximum frames per second the game can render.  						|
| `generation_max_time`            | int        | Maximum time (in minutes) for a generation to run.					|
| `game_screen_width`              | int        | Width of the game screen in pixels.             						|
| `game_screen_height`             | int        | Height of the game screen in pixels.            						|
| `config_menu_visible`            | boolean    | Indicates if the configuration menu is visible. 						|
| `config_menu_screen_width`       | int        | Width of the configuration menu in pixels.      						|
| `config_menu_screen_height`      | int        | Height of the configuration menu in pixels.   		  				|
| `cars_quantity`                  | int        | Number of cars in the game.                   		   				|
| `cars_rays_count`                | int        | Number of rays used for car detection.         		  				|
| `cars_max_speed`                 | int        | Maximum speed of the cars.                     		  				|
| `mutation_chance_random_value`   | int        | chance to give a car a random DNA.               						|
| `mutation_chance_scale_value`    | int        | chance to give the DNA of the best car multiplied by a random number. |
| `mutation_chance_add_value`      | int        | chance to give the DNA of the best car plus a random number.      	|

| Option                           | Requisites/Default		     |
|----------------------------------|-----------------------------|
| `fps_limit`                      | x >  0 / 60.0               |
| `generation_max_time`            | x >  0 / 120	             |
| `cars_quantity`                  | x >  7 / 15                 |
| `cars_rays_count`                | x >= 9 / 9                  |
| `cars_max_speed`                 | x >  1 / 10                 |
| `mutation_chance_random_value`   | x >= 0 & x <= 100  / 10     |
| `mutation_chance_scale_value`    | x >= 0 & x <= 100  / 55     |
| `mutation_chance_add_value`      | x >= 0 & x <= 100  / 35     |

## Notes
- Ensure that values are set within reasonable limits to maintain game performance and balance.
- Adjust `fps_limit` according to the target hardware capabilities.
- The `generation_max_time` may need to be tuned based on the desired pacing of the game.
- The sum of the mutation percentages must equal 100%. If the total is less than or greater than 100%, the values will be set to the default percentages. (x% + y% + z% => 100%)
- For better training of the neural network, I recommend using at least 20 cars.
