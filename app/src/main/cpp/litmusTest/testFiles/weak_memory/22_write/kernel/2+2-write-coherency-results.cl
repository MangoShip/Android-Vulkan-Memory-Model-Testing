static uint permute_id(uint id, uint factor, uint mask) {
  return (id * factor) % mask;
}

static uint stripe_workgroup(uint workgroup_id, uint local_id, uint testing_workgroups) {
  return (workgroup_id + 1 + local_id % (testing_workgroups - 1)) % testing_workgroups;
}

__kernel void litmus_test (
  __global atomic_uint* test_locations,
  __global atomic_uint* read_results,
  __global atomic_uint* test_results,
  __global uint* stress_params) {
  uint id_0 = get_global_id(0);
  uint x_0 = (id_0) * stress_params[10] * 2;
  uint mem_x_0 = atomic_load(&test_locations[x_0]);
  uint r0 = atomic_load(&read_results[id_0 * 2]);
  uint r1 = atomic_load(&read_results[id_0 * 2 + 1]);
  uint total_ids = get_local_size(0) * stress_params[9];
  uint y_0 = (permute_id(id_0, stress_params[8], total_ids)) * stress_params[10] * 2 + stress_params[11];
  uint mem_y_0 = atomic_load(&test_locations[y_0]);
  /*if ((mem_x_0 == 2 && mem_y_0 == 1)) {
    atomic_fetch_add(&test_results[0], 1);
  } else if ((mem_x_0 == 4 && mem_y_0 == 3)) {
    atomic_fetch_add(&test_results[1], 1);
  } else if ((mem_x_0 == 4 && mem_y_0 == 1)) {
    atomic_fetch_add(&test_results[2], 1);
  } else if ((mem_x_0 == 2 && mem_y_0 == 3)) {
    atomic_fetch_add(&test_results[3], 1);
  }*/
  if ((mem_x_0 == 1 && mem_y_0 == 1)) {
    atomic_fetch_add(&test_results[0], 1);
  } else if ((mem_x_0 == 4 && mem_y_0 == 4)) {
    atomic_fetch_add(&test_results[1], 1);
  } else if ((mem_x_0 == 2 && mem_y_0 == 2)) {
    atomic_fetch_add(&test_results[3], 1);
  } else if ((mem_x_0 == 3 && mem_y_0 == 3)) {
    atomic_fetch_add(&test_results[3], 1);
  }
}
    